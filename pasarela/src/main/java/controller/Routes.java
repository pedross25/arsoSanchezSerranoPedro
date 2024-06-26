package controller;

public class Routes {
    public static final String BASE_URL = "http://localhost:5047/api/usuarios";
    public static final String BASE_URL_DOCKER_MAC = "http://host.docker.internal:5047/api/usuarios";

    public static final String VERIFY_CREDENTIALS = BASE_URL_DOCKER_MAC + "/verificar-credenciales";
    public static final String VERIFY_OAUTH2 = BASE_URL_DOCKER_MAC + "/verificar-oauth2";

}