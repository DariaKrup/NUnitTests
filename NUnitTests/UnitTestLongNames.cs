using System.Reflection;

namespace NUnitTests.Namespace1.Namespace2.Namespace3.Namespace4.Namespace5.Namespace6.Namespace7.ThisIsAVeryLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongNamespaceName;

public class UnitTestsLongNames
{
    
    [Test]
    public void ThisIsTestWithNameLongerThanDirectoryMaxAllowedLength12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890()
    {
        var assembly = Assembly.GetExecutingAssembly();
        string resourceName = "NUnitTests.TestResources.avatar.jpg";

        using (Stream resourceStream = assembly.GetManifestResourceStream(resourceName))
        {
            Assert.IsNotNull(resourceStream, "Image file not found or could not be loaded.");

            string tempFilePath = Path.Combine(Path.GetTempPath(), "image_avatar.jpg");

            using (FileStream fileStream = File.Create(tempFilePath))
            {
                resourceStream.CopyTo(fileStream);
            }

            Assert.IsTrue(File.Exists(tempFilePath), "Temporary file was not created.");

            TestContext.AddTestAttachment(tempFilePath, "An avatar");
        }
    }
}
