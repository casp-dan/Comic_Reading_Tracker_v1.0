package app.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses
({
    DateTests.class,
    EntryTests.class,
    
})
public class ModelTestSuite
{ // no implementation needed; above annotations do the work.
}