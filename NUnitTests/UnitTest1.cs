using System.Reflection;

namespace NUnitTestsWithAttachments;

public class Tests
{
    [SetUp]
    public void Setup()
    {
    }

    [Test]
    public void Test1()
    {
        Assert.Pass();
    }
    
    [Test]
    public void SuccessfulTestWithAnImage()
    {
        var assembly = Assembly.GetExecutingAssembly();
        string resourceName = "NUnitTestsWithAttachments.TestResources.cat.jpg";

        using (Stream resourceStream = assembly.GetManifestResourceStream(resourceName))
        {
            Assert.IsNotNull(resourceStream, "Image file not found or could not be loaded.");

            string tempFilePath = Path.Combine(Path.GetTempPath(), "image_cat.jpg");

            using (FileStream fileStream = File.Create(tempFilePath))
            {
                resourceStream.CopyTo(fileStream);
            }

            Assert.IsTrue(File.Exists(tempFilePath), "Temporary file was not created.");

            TestContext.AddTestAttachment(tempFilePath, "A cat");
        }
    }
    
    [Test]
    public void FailedTestWithAnImage()
    {
        var assembly = Assembly.GetExecutingAssembly();
        string resourceName = "NUnitTestsWithAttachments.TestResources.404.png";

        using (Stream resourceStream = assembly.GetManifestResourceStream(resourceName))
        {
            Assert.IsNotNull(resourceStream, "Image file not found or could not be loaded.");

            string tempFilePath = Path.Combine(Path.GetTempPath(), "image_404.png");

            using (FileStream fileStream = File.Create(tempFilePath))
            {
                resourceStream.CopyTo(fileStream);
            }

            Assert.IsTrue(File.Exists(tempFilePath), "Temporary file was not created.");

            TestContext.AddTestAttachment(tempFilePath, "NOT FOUND!!!");
            
            Assert.Fail("Sorry, test is failed");
        }
    }

    [Test] public void TestWithATextFile()
    {
        var assembly = Assembly.GetExecutingAssembly();
        string resourceName = "NUnitTestsWithAttachments.TestResources.file.rtf";

        using (Stream resourceStream = assembly.GetManifestResourceStream(resourceName))
        {
            Assert.IsNotNull(resourceStream, "File file not found or could not be loaded.");

            string tempFilePath = Path.Combine(Path.GetTempPath(), "file.rtf");

            using (FileStream fileStream = File.Create(tempFilePath))
            {
                resourceStream.CopyTo(fileStream);
            }

            Assert.IsTrue(File.Exists(tempFilePath), "Temporary file was not created.");

            TestContext.AddTestAttachment(tempFilePath, "A file)");
        }
    }
}