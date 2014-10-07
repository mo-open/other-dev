package mds.test;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Dongsong
 */
public class TestEnumClone implements Cloneable {
    private ClonedEnum state=ClonedEnum.INIT;
    public enum ClonedEnum {
        INIT(0), START(1);
        private int value;

        private ClonedEnum(int value) {
            this.value = value;
        }
    }

    @Test
    public void runTest() throws Exception {
        TestEnumClone testEnumClone=new TestEnumClone();
        testEnumClone.state=ClonedEnum.START;
        TestEnumClone cloneEnum=(TestEnumClone)testEnumClone.clone();
        Assert.assertEquals(testEnumClone.state,cloneEnum.state);
    }
}
