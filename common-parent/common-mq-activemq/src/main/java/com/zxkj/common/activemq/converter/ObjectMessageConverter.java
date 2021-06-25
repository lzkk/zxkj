package com.zxkj.common.activemq.converter;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.*;

public class ObjectMessageConverter implements MessageConverter {
	
	@Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream out = null;
        try {
            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);

            out.writeObject(object);

            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(bos.toByteArray());

            return msg;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(out);
            close(bos);
        }
    }

	@Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        Object object = null;
        if (message instanceof ObjectMessage) {
            ByteArrayInputStream ois = null;
            ObjectInputStream in = null;
            try {
                ois = new ByteArrayInputStream((byte[]) ((ObjectMessage) message).getObject());
                in = new ObjectInputStream(ois);
                object = in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(in);
                close(ois);
            }
        }

        return object;
    }

    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
