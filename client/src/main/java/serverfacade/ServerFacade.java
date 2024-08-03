package serverfacade;

public class ServerFacade {

    private final String url;

    public ServerFacade(String url){
        this.url = url;
    }

    public void clear(){}

    public Object register(){
        return null;
    }

    public Object login(){
        return null;
    }

    public Object logout(){
        return null;
    }

    public Object listGames(){
        return null;
    }

    public Object createGame(){
        return null;
    }

    public Object joinGame(){
        return null;
    }

    private <T> T makeRequest(){
        return null;
    }

    private static void writeBody(){}

    private static <T> T readBody(){
        return null;
    }
}
