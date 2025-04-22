file://<WORKSPACE>/src/test/java/com/absa/utils/TestDataUtil.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/test/java/com/absa/utils/TestDataUtil.java
text:
```scala
package com.absa.utils;

import com.absa.models.UserData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestDataUtil {
    private static UserData latestUser;

    // Reads user data from a CSV file. Assumes the first row is a header.
    public static UserData getUserDataFromCsv(String fileName, int rowIndex) {
        List<UserData> users = new ArrayList<>();
        String filePath = "src/test/resources/testdata/" + fileName;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // CSV uses comma separator.
                // CSV columns: FirstName,LastName,UserName,Password,Company,Role,Email,Mobilephone
                if (data.length >= 8) {
                    UserData user = new UserData(
                        data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(),
                        data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim()
                    );
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users.get(rowIndex);
    }

    // Save the latest user for later verification.
    public static void setLatestUser(UserData user) {
        latestUser = user;
    }

    public static UserData getLatestUser() {
        return latestUser;
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator