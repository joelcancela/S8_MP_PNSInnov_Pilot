package unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions;

/**
 * Created by Nassim B on 6/4/18.
 */
public class UnhandledMimeTypeRException extends RuntimeException {
	public UnhandledMimeTypeRException() {
		super("A classifying rules has been triggered on an unhandled mimetype");
	}
}
