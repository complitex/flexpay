package org.flexpay.common.service.reporting;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BarbecueRenderer extends JRAbstractSvgRenderer
{
    private boolean rotate;
    private Barcode barcode = null;

    public BarbecueRenderer(Barcode barcode)
    {
        this(barcode, false);
    }

    public BarbecueRenderer(Barcode barcode, boolean rotate)
    {
        this.barcode = barcode;
        this.rotate = rotate;
    }

    @Override
    public void render(Graphics2D grx, Rectangle2D rectangle)  throws JRException
    {
        if (barcode != null)
        {
            Graphics2D graphics = (Graphics2D) grx.create();
            graphics.translate(rectangle.getX(), rectangle.getY());
            if (rotate)
            {
                graphics.translate(barcode.getBounds().getHeight(), 0);
                graphics.rotate(Math.PI / 2);
            }

			try {
            	barcode.draw(graphics, 0, 0);
			} catch (OutputException e) {
				throw new JRException("Failed draw barcode", e);
			}
        }
    }
}
