package org.flexpay.validatorregistry.util;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class TextAreaWriter extends OutputStream {

	volatile JTextArea textArea;
	private volatile StringBuilder sb = new StringBuilder();
	private int maximumConsoleLines;

	public TextAreaWriter(JTextArea textArea, int maximumConsoleLines) {
		this.textArea = textArea;
		this.maximumConsoleLines = maximumConsoleLines;
	}

	/**
	 * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is written to
	 * the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>. The 24 high-order bits
	 * of <code>b</code> are ignored.
	 * <p/>
	 * Subclasses of <code>OutputStream</code> must provide an implementation for this method.
	 *
	 * @param b the <code>byte</code>.
	 * @throws java.io.IOException if an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the output
	 *                             stream has been closed.
	 */
	public void write(int b) throws IOException {

		if (b == '\r') {
			return;
        }

		if (b == '\n') {

			if (textArea.getLineCount() > maximumConsoleLines){
				String currentText = textArea.getText();
				textArea.setText(currentText.substring(currentText.indexOf("\n",1)));
			}
			textArea.setCaretPosition(textArea.getDocument().getLength());
			textArea.append(sb.toString());
			textArea.repaint();
			sb.setLength(0);
		}
		sb.append((char)b);

	}

}
