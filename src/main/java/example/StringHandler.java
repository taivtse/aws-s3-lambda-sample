package example;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringHandler implements RequestHandler<String, Object> {

    private String lastInput;

    @Override
    public Object handleRequest(String input, Context context) {
        log.info("Running StringHandler::handleRequest");
        log.info("lastInput: {}", lastInput);
        log.info("currentInput: {}", input);
//        context.getLogger().log("Input: " + input);
        lastInput = input;
        return "Lambda Function is invoked, input = " + input;
    }
}