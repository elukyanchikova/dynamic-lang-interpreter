package syntaxer.entities;

import syntaxer.entities.Body;

public class BodyFunctionBody extends FunctionBody {
    private Body body;

    public BodyFunctionBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
