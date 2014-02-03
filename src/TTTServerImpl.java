import javax.swing.event.ListSelectionEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tib on 2/3/14.
 */
public class TTTServerImpl extends UnicastRemoteObject implements TTTServer {

    private TicTacToe tttgame;


    public TTTServerImpl(TicTacToe tttgame) throws RemoteException{
        this.tttgame = tttgame;
    }

    @Override
    public void connect(String name, char mark, TTTServerImpl opponent){

    }

    public void valueChanged(ListSelectionEvent e){
        tttgame.valueChanged(e);

    }



}
