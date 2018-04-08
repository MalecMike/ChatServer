import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerLogic {
    private boolean servOn = true;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerGUI serverGui;
    private ServerWorker serverWorker;
    private List<ServerWorker> workerList;

    public ServerLogic(int port,ServerGUI serverGUI){
        this.port = port;
        this.serverGui = serverGUI;
        workerList = new LinkedList<>();
    }
    public void startRunning() throws IOException {
        serverSocket = new ServerSocket(port);
        while(servOn){
            waitForConnection();
            serverWorker = new ServerWorker(this,socket);
            Thread thread = new Thread(serverWorker);
            workerList.add(serverWorker);
            thread.start();
        }
    }
    public ServerGUI getServerGui() {
        return serverGui;
    }
    public void waitForConnection() throws IOException {
        showMessage("Waiting for connections");
        socket = serverSocket.accept();
        showMessage("Now connected to " + socket.getInetAddress().getHostName());
    }
    public ServerWorker getServerWorker() {
        return serverWorker;
    }
    public void showMessage(String message){
        serverGui.setChatWindow(message + "\n");
    }
    public List<ServerWorker> getWorkerList() {
        return workerList;
    }
    public void removeWorkerFromList(){
        for(ServerWorker worker :workerList){
            if(worker.getConnection() != true){
                workerList.remove(worker);
                showMessage("User Left");
            }
        }
    }
}