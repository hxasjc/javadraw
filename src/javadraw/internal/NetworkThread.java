//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

abstract class NetworkThread implements Runnable {
    private static final int MAX_MESSAGE_LENGTH = 65536;
    private static final String LOST = "Lost";
    private static final int IDLE_SLEEP_TIME = 10;
    static final String CHARSET_NAME = "ISO-8859-1";
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    private static final boolean DEBUG = false;
    private final CharsetEncoder encoder;
    private final CharsetDecoder decoder;
    private final List channelChanges;
    private final List messagesToWrite;
    private final ArrayList channels;
    private final LinkedList POOL;
    private final CharBuffer readCharBuffer;
    final Selector selector;
    private final String name;
    private final Message nameMessage;
    private boolean running;
    private int nextID;

    private Message encodeShortMessage(String string) {
        CharBuffer input = CharBuffer.wrap(string);
        Message message = this.createMessage();
        ByteBuffer buffer = message.buffer;
        buffer.position(2);
        this.encoder.reset();
        this.encoder.encode(input, buffer, true);
        this.encoder.flush(buffer);
        int length = buffer.position();
        buffer.flip();
        buffer.put((byte)(length >> 8));
        buffer.put((byte)(length & 255));
        buffer.rewind();
        return message;
    }

    public String getName() {
        return this.name;
    }

    void start() {
        (new Thread(this)).start();
    }

    NetworkThread(String name) throws IOException {
        this.encoder = CHARSET.newEncoder();
        this.decoder = CHARSET.newDecoder();
        this.channelChanges = Collections.synchronizedList(new LinkedList());
        this.messagesToWrite = Collections.synchronizedList(new LinkedList());
        this.channels = new ArrayList();
        this.POOL = new LinkedList();
        this.readCharBuffer = CharBuffer.allocate(65536);
        this.running = false;
        this.nextID = 0;
        this.selector = Selector.open();
        this.name = name;
        this.nameMessage = this.encodeShortMessage(name);
        this.nameMessage.retainCount = -1;
    }

    private void release(Message message) {
        if (--message.retainCount == 0) {
            this.POOL.add(message);
        }

    }

    private Message createMessage() {
        Message message = this.POOL.isEmpty() ? new Message() : (Message)this.POOL.removeFirst();
        message.buffer.clear();
        return message;
    }

    private String decode(ArrayList messages) {
        this.readCharBuffer.clear();
        this.decoder.reset();
        ((Message)messages.get(0)).buffer.position(2);

        for(int i = 0; i < messages.size(); ++i) {
            ByteBuffer buffer = ((Message)messages.get(i)).buffer;
            if (i == messages.size() - 1) {
                this.decoder.decode(buffer, this.readCharBuffer, true);
            } else {
                this.decoder.decode(buffer, this.readCharBuffer, false);
            }
        }

        this.decoder.flush(this.readCharBuffer);
        this.readCharBuffer.flip();
        return this.readCharBuffer.toString();
    }

    private ArrayList encode(String string, int retainCount) {
        CharBuffer input = CharBuffer.wrap(string);
        ArrayList messages = new ArrayList();
        Message firstMessage = this.createMessage();
        firstMessage.buffer.position(2);
        messages.add(firstMessage);
        firstMessage.retainCount = retainCount;
        Message message = firstMessage;
        int length = 0;
        this.encoder.reset();

        for(CoderResult result = this.encoder.encode(input, firstMessage.buffer, true); result.isOverflow(); result = this.encoder.encode(input, message.buffer, true)) {
            length += message.buffer.position();
            message.buffer.flip();
            messages.add(message = this.createMessage());
            message.retainCount = retainCount;
        }

        for(CoderResult var10 = this.encoder.flush(message.buffer); var10.isOverflow(); var10 = this.encoder.flush(message.buffer)) {
            length += message.buffer.position();
            message.buffer.flip();
            messages.add(message = this.createMessage());
            message.retainCount = retainCount;
        }

        length += message.buffer.position();
        message.buffer.flip();
        firstMessage.buffer.put((byte)(length >> 8));
        firstMessage.buffer.put((byte)(length & 255));
        firstMessage.buffer.rewind();
        return messages;
    }

    private Channel getChannel(int id) {
        if (this.channels.isEmpty()) {
            return null;
        } else {
            int max = Math.min(id + 1, this.channels.size());
            int min = 0;

            while(min + 1 != max) {
                int mid = (max + min) / 2;
                if (id >= ((Channel)this.channels.get(mid)).id) {
                    min = mid;
                } else {
                    max = mid;
                }
            }

            Channel found = (Channel)this.channels.get(min);
            return found.id == id ? found : null;
        }
    }

    abstract void messageReceived(int var1, String var2, String var3);

    abstract void channelClosed(int var1, String var2);

    abstract void channelReady(int var1, String var2);

    void updateChannelLater(SelectableChannel channel, int interestOps, int changeMask) {
        SelectionKey key = channel.keyFor(this.selector);
        this.channelChanges.add(new ChannelUpdate(key, interestOps, changeMask));
    }

    public void shutDown() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    void enqueueMessage(String message, int channelID) {
        this.messagesToWrite.add(new PendingMessage(message, channelID));
        this.selector.wakeup();
    }

    private void handlePendingMessages() {
        while(!this.channelChanges.isEmpty()) {
            ChannelUpdate update = (ChannelUpdate)this.channelChanges.remove(0);
            update.key.interestOps(update.key.interestOps() & ~update.changeMask | update.interestOps & update.changeMask);
        }

        while(!this.messagesToWrite.isEmpty()) {
            PendingMessage message = (PendingMessage)this.messagesToWrite.remove(0);
            if (message.channel < 0) {
                if (!this.channels.isEmpty()) {
                    ArrayList messages = this.encode(message.message, this.channels.size());

                    for(int i = 0; i < this.channels.size(); ++i) {
                        Channel channel = (Channel)this.channels.get(i);
                        if (!channel.enqueue(messages)) {
                            --i;
                        }
                    }
                }
            } else {
                Channel channel = this.getChannel(message.channel);
                if (channel != null) {
                    channel.enqueue(this.encode(message.message, 1));
                }
            }
        }

    }

    private void newChannel(SocketChannel socketChannel) {
        try {
            socketChannel.configureBlocking(false);
            Channel channel = new Channel(socketChannel);
            SelectionKey key = socketChannel.keyFor(this.selector);
            if (key == null) {
                socketChannel.register(this.selector, 5, channel);
            } else {
                key.attach(channel);
                key.interestOps(5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void performSelection() {
        try {
            if (this.selector.select() == 0) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Iterator iter = this.selector.selectedKeys().iterator();

        while(iter.hasNext()) {
            SelectionKey key = (SelectionKey)iter.next();
            iter.remove();
            if (key.isAcceptable()) {
                try {
                    this.newChannel(((ServerSocketChannel)key.channel()).accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (key.isConnectable()) {
                SocketChannel channel = (SocketChannel)key.channel();

                try {
                    if (!channel.finishConnect()) {
                        throw new RuntimeException("Connection failed to finish.");
                    }

                    this.newChannel(channel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ChannelHandler channel = (ChannelHandler)key.attachment();
                if (channel != null) {
                    if (key.isReadable()) {
                        channel.read();
                    }

                    if (key.isValid() && key.isWritable()) {
                        channel.write();
                    }
                }
            }
        }

    }

    void closeAll() {
        try {
            this.selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!this.channels.isEmpty()) {
            ((Channel)this.channels.get(this.channels.size() - 1)).close();
        }

    }

    boolean setup() {
        return true;
    }

    void tearDown() {
    }

    /** @deprecated */
    public void run() {
        this.running = this.setup();

        while(this.running) {
            try {
                this.handlePendingMessages();
                this.performSelection();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        this.tearDown();
        this.closeAll();
    }

    private class Channel implements ChannelHandler {
        private String name = null;
        private final int id;
        private final ArrayList readBuffers = new ArrayList();
        private final LinkedList writeQueue = new LinkedList();
        private final SocketChannel channel;
        private int remainingReadLength;
        private int writePosition;

        public Channel(SocketChannel channel) {
            this.channel = channel;
            this.readBuffers.add(NetworkThread.this.createMessage());
            this.remainingReadLength = -1;
            this.writeQueue.add(NetworkThread.this.nameMessage);
            this.writePosition = -1;
            this.id = NetworkThread.this.nextID++;
            NetworkThread.this.channels.add(this);
        }

        private void setWriteInterest(boolean interested) {
            SelectionKey key = this.channel.keyFor(NetworkThread.this.selector);
            key.interestOps(interested ? key.interestOps() | 4 : key.interestOps() & -5);
        }

        public boolean enqueue(ArrayList messages) {
            if (!this.channel.isOpen()) {
                this.close();
                return false;
            } else {
                if (this.writeQueue.isEmpty()) {
                    this.setWriteInterest(true);
                }

                this.writeQueue.addAll(messages);
                return true;
            }
        }

        public boolean readOnePage() throws IOException {
            ByteBuffer buffer = ((Message)this.readBuffers.get(this.readBuffers.size() - 1)).buffer;
            int readBytes = this.channel.read(buffer);
            if (readBytes == -1) {
                this.close();
                return false;
            } else if (readBytes == 0) {
                return false;
            } else {
                int position = buffer.position();
                boolean moreToRead = position == buffer.capacity();
                if (this.remainingReadLength == -1) {
                    if (position < 2) {
                        return false;
                    }

                    buffer.position(0);
                    this.remainingReadLength = (buffer.get() & 255) << 8 | buffer.get() & 255;
                    buffer.position(position);
                }

                if (position == this.remainingReadLength) {
                    buffer.flip();
                    this.finishRead();
                    this.readBuffers.add(NetworkThread.this.createMessage());
                    this.remainingReadLength = -1;
                } else if (position > this.remainingReadLength) {
                    while(position >= this.remainingReadLength) {
                        Message nextMessage = NetworkThread.this.createMessage();
                        buffer.flip();
                        if (position > this.remainingReadLength) {
                            buffer.position(this.remainingReadLength);
                            nextMessage.buffer.put(buffer);
                            buffer.rewind();
                            buffer.limit(this.remainingReadLength);
                        }

                        this.finishRead();
                        this.readBuffers.add(nextMessage);
                        buffer = nextMessage.buffer;
                        position = buffer.position();
                        if (position < 2) {
                            this.remainingReadLength = -1;
                            break;
                        }

                        buffer.position(0);
                        this.remainingReadLength = (buffer.get() & 255) << 8 | buffer.get() & 255;
                        buffer.position(position);
                    }
                } else if (moreToRead) {
                    buffer.flip();
                    this.remainingReadLength -= position;
                    this.readBuffers.add(NetworkThread.this.createMessage());
                }

                return moreToRead;
            }
        }

        private void finishRead() {
            if (this.name == null) {
                this.name = NetworkThread.this.decode(this.readBuffers);
                NetworkThread.this.channelReady(this.id, this.name);
            } else {
                NetworkThread.this.messageReceived(this.id, this.name, NetworkThread.this.decode(this.readBuffers));
            }

            NetworkThread.this.POOL.addAll(this.readBuffers);
            this.readBuffers.clear();
        }

        public void read() {
            try {
                while(this.readOnePage()) {
                }

            } catch (IOException e) {
                this.close();
                e.printStackTrace();
            }
        }

        public void close() {
            NetworkThread.this.channelClosed(this.id, this.name);
            NetworkThread.this.channels.remove(this);

            while(!this.writeQueue.isEmpty()) {
                NetworkThread.this.release((Message)this.writeQueue.removeFirst());
            }

            try {
                this.channel.close();
            } catch (IOException var2) {
            }

        }

        public void write() {
            try {
                while(!this.writeQueue.isEmpty()) {
                    ByteBuffer buffer = ((Message)this.writeQueue.getFirst()).buffer;
                    if (this.writePosition >= 0) {
                        buffer.position(this.writePosition);
                        this.writePosition = -1;
                    } else {
                        buffer.rewind();
                    }

                    try {
                        this.channel.write(buffer);
                    } catch (ClosedChannelException var3) {
                        this.close();
                        return;
                    }

                    if (buffer.remaining() > 0) {
                        this.writePosition = buffer.position();
                        return;
                    }

                    NetworkThread.this.release((Message)this.writeQueue.removeFirst());
                }

                this.setWriteInterest(false);
            } catch (IOException e) {
                this.close();
                e.printStackTrace();
            }
        }
    }

    private class Message {
        final ByteBuffer buffer;
        int retainCount;

        private Message() {
            this.buffer = ByteBuffer.allocateDirect(1024);
            this.retainCount = 0;
        }
    }

    private static class PendingMessage {
        public final String message;
        public final int channel;

        public PendingMessage(String message, int channel) {
            this.message = message;
            this.channel = channel;
        }
    }

    private class ChannelUpdate {
        private final SelectionKey key;
        private final int interestOps;
        private final int changeMask;

        public ChannelUpdate(SelectionKey key, int interestOps, int changeMask) {
            this.key = key;
            this.interestOps = interestOps;
            this.changeMask = changeMask;
        }
    }

    interface ChannelHandler {
        void read();

        void write();
    }
}
