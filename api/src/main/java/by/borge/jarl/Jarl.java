package by.borge.jarl;

import by.borge.jarl.internal.IntermediateRepresentation;
import by.borge.jarl.internal.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A collection of compiled {@link Plan}s, indexed by the entrypoints in the input IR.
 */
public class Jarl {
    private final IntermediateRepresentation ir;

    private Jarl(IntermediateRepresentation ir) {
        this.ir = ir;
    }

    /**
     * Gets the executable {@link Plan} indexed by <code>entryPoint</code>.
     *
     * @param entryPoint the "name" of the wanted Plan
     * @return the Plan corresponding to <code>entryPoint</code>
     */
    public Plan getPlan(String entryPoint) {
        var plan = ir.getPlan(entryPoint);
        if (plan == null) {
            throw new IllegalArgumentException(String.format("No plan found for entry-point: %s", entryPoint));
        }
        return new Plan(plan);
    }

    public static Builder builder(File irFile) {
        return new Builder(Files.readString(irFile.toPath()));
    }

    public static Builder builder(String rawIr) {
        return new Builder(rawIr);
    }

    public static class Builder {
        private final String rawIr;
        private boolean strict = false;

        public Builder(String irRaw) {
            this.rawIr = rawIr;
        }

        public Builder strictBuiltinErrors(boolean strict) {
            this.strict = strict;
            return this;
        }

        public Jarl build() throws IOException {
            var ir = Parser.parse(rawIr)
                    .withStrictBuiltinErrors(strict);
            return new Jarl(ir);
        }
    }
}
