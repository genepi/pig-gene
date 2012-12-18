package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowWriter {
	public static void write(final ArrayList<WorkflowComponent> workflow, final String name) throws IOException {

		// ObjectOutput out = null;
		// try {
		// out = new ObjectOutputStream(new
		// FileOutputStream(name.concat(".txt")));

		YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream("lukas")));
		System.out.println(workflow.isEmpty());
		writer.write(workflow);
		writer.close();
		// out.writeObject(workflow);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		// try {
		// // out.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

}