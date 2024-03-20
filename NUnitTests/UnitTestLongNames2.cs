using System.Reflection;

namespace NUnitTestsWithAttachments.Namespace1.Namespace2.Namespace3;

public class UnitTestsLongNames2
{
    
    [Test]
    public void ThisIsTestWithNameNotLongerThanDirectoryMaxAllowedLengthButWithUnderscores_ABCDEFGHIJK_ABCDEFGHIJK_ABCDEFGHIJK__ABCDEFGHIJK_ABCDEFGHIJK_____ABCDEFGHIJK_ABCDEFGHIJK_____ABCDEFGHIJK_ABCDEFGHIJK_ABCD()
    {
        var assembly = Assembly.GetExecutingAssembly();
        string resourceName = "NUnitTestsWithAttachments.TestResources.avatar.jpg";

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