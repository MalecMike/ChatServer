import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerWorker implements Runnable {
    private ServerLogic serverLogic;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private List<ServerWorker> list;
    private boolean connected;

    public ServerWorker(ServerLogic serverLogic,Socket socket){
        this.serverLogic = serverLogic;
        this.socket = socket;
        list  = serverLogic.getWorkerList();
        connected = true;
    }
    private void connectionInThread(){
        try {
            setUpStreams();
            gettingMessages();
        } catch (IOException e) {
            System.out.println("error");
        }finally {
            closeConnection();
            serverLogic.removeWorkerFromList();
        }
    }
    private void setUpStreams() throws IOException {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }
    private void gettingMessages() throws IOException{
        String message = "You are now connected\n";
        sendMessageToAllClients(message);
        serverLogic.getServerGui().setCanType(true);
        do{
            try {
                message = (String) objectInputStream.readObject();
                List<ServerWorker> serverWorkers = serverLogic.getWorkerList();
                showMessage(message);
                if(serverWorkers.size() > 1){
                    for(ServerWorker worker : serverWorkers){
                        worker.sendMessageToAllClients(message);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }while(!message.contains("bye"));
    }
    private void sendMessageToAllClients(String message){
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showMessage(String message){
        serverLogic.getServerGui().setChatWindow(message + "\n");
    }
    public void sendToAllServerMessage(String message){
        showMessage(message);
        for(ServerWorker worker : list){
            worker.sendMessageToAllClients(message);
        }
    }
    private void closeConnection(){
        try {
            socket.close();
            objectOutputStream.close();
            objectInputStream.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        connectionInThread();
    }
    public boolean getConnection(){
        return connected;
    }
}