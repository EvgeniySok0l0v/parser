package clevertec.by.sokalau;

import clevertec.by.sokalau.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.List;

public class ParserTest {


    @ParameterizedTest
    @ValueSource(strings = {
            "java.lang.String",
            "int",
            "java.lang.Boolean",
            "java.lang.String[]",
            "int[]",
            "java.util.List",
            "java.lang.Object",
            "clevertec.sokalau.by.Parser"
        }
    )
    void checkWorkWithField(String str){
        StringUtils.workWithField(str);
    }

}
