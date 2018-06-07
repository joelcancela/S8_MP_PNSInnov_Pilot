package unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions;

/**
 * Created by Nassim B on 6/4/18.
 */
public class UnhandledExtensionRException extends RuntimeException {
	public UnhandledExtensionRException() {
		super("A classifying rules has been triggered on an unhandled extension");
	}
}
