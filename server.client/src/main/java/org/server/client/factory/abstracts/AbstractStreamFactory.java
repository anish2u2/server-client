package org.server.client.factory.abstracts;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import org.server.client.contract.Reader;
import org.server.client.contract.Writer;
import org.server.client.factory.contracts.StreamFactory;
import org.server.client.imple.reader.ReaderImpl;
import org.server.client.imple.writer.WriterImple;

public abstract class AbstractStreamFactory implements StreamFactory {

	private List<WeakReference<Writer>> writerPool = new LinkedList<WeakReference<Writer>>();
	private List<WeakReference<Reader>> readerPool = new LinkedList<WeakReference<Reader>>();

	public Writer getWriteStream() {

		Writer writer = findWriter();
		if (writer == null)
			writer = new WriterImple();
		return writer;
	}

	private Writer findWriter() {

		Writer writerObject = null;
		try {
			for (WeakReference<Writer> writer : writerPool) {
				writer.get().lock();
				if (writer.get().isClosed()) {
					writerObject = writer.get();
				}
				if (writerObject != null) {
					writer.get().unlock();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return writerObject;
	}

	public Reader getReader() {
		Reader reader = findReader();
		if (reader == null)
			reader = new ReaderImpl();
		return reader;
	}

	private Reader findReader() {

		Reader readerObject = null;
		try {
			for (WeakReference<Reader> reader : readerPool) {
				reader.get().lock();
				if (reader.get().isClosed()) {
					readerObject = reader.get();
				}
				if (readerObject != null) {
					reader.get().unlock();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return readerObject;
	}
}
