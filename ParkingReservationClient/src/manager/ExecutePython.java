package manager;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExecutePython {
	
	public String execute(String folder, String filename, String[] arguments) {
		String result = "Error: Python Error";
		
		try {
            String order = "python";
            String fileroot = folder + "/" + filename;
            
			CommandLine commandLine = CommandLine.parse(order);
			commandLine.addArgument(fileroot);
			
			for(int i=0; i<arguments.length; i++)
				commandLine.addArgument(arguments[i]);

	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
	        DefaultExecutor executor = new DefaultExecutor();
	        executor.setStreamHandler(pumpStreamHandler);
	        
	        int success = executor.execute(commandLine);
	        
	        if(success == 0) {
	        	result = outputStream.toString();
	        	System.out.println("output: " + result);
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
    }
}