package unice.polytech.si4.pnsinnov.teamm.api;

/**
 * Created by Nassim B on 6/4/18.
 */
public class User {
	private String idUser;
	private StorageSession session;

	public User(String idUser, StorageSession session) {
		this.idUser = idUser;
		this.session = session;
	}

	public StorageSession getSession() {
		return session;
	}
}
