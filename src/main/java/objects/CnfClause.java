package objects;

import java.util.Set;
import java.util.stream.Collectors;

public class CnfClause {
    private Set<CnfVar> cnfs;

    public CnfClause(Set<CnfVar> cnfs) {
        this.cnfs = cnfs;
    }

    @Override
    public String toString() {
        return String.join(" ", cnfs.stream().map(CnfVar::toString).collect(Collectors.toList()));
    }
}
