package unice.polytech.si4.pnsinnov.teamm.drools.exceptions;

/**
 * Created by Nassim B on 6/4/18.
 */
public class UnhandledExtension extends RuntimeException {
	public UnhandledExtension() {
		super("A classifying rules has been triggered on an unhandled extension");
	}
}
