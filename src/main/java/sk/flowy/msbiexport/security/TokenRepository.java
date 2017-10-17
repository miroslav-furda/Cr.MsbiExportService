package sk.flowy.msbiexport.security;

public interface TokenRepository {
    CallResponse checkToken(String requestToken, boolean tokenExpired);
}
