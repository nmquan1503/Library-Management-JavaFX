package org.example.demo.Controllers;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaseControllerTest {

  @BeforeEach
  void resetStateBefore() {
    BaseController.setBaseState(1);
    BaseController.setLibId(-1);
    BaseController.setDueUpdate(0);
  }

  @AfterEach
  void resetStateAfter() {
    BaseController.setBaseState(1);
    BaseController.setLibId(-1);
    BaseController.setDueUpdate(0);
  }

  @Test
  void getBaseState() {
    assertEquals(1, BaseController.getBaseState(), "Initial base state should be 1");
  }

  @Test
  void setBaseState() {
    BaseController.setBaseState(5);
    assertEquals(5, BaseController.getBaseState(), "Base state should be updated to 5");
  }

  @Test
  void getLibId() {
    assertEquals(-1, BaseController.getLibId(), "Initial libId should be -1");
  }

  @Test
  void setLibId() {
    BaseController.setLibId(10);
    assertEquals(10, BaseController.getLibId(), "LibId should be updated to 10");
  }

  @Test
  void getIsBorrowingChanged() {
    assertEquals(0, BaseController.getDueUpdate(), "Initial borrowing change state should be 0");
  }

  @Test
  void setIsBorrowingChanged() {
    BaseController.setDueUpdate(1);
    assertEquals(1, BaseController.getDueUpdate(), "Borrowing change state should be updated to 1");
  }

  @Test
  void testGenerateQRCodeImage() {
    String mailtoLink = "mailto:test@example.com?subject=Test&body=This is a test";
    int width = 300;
    int height = 300;

    WritableImage qrImage = BaseController.generateQRCodeImage(mailtoLink, width, height);

    assertNotNull(qrImage, "QR code image should not be null");
    assertEquals(width, (int) qrImage.getWidth(), "QR code image width should match");
    assertEquals(height, (int) qrImage.getHeight(), "QR code image height should match");
  }

  @Test
  void testQRCodeNonEmptyPixels() {
    String mailtoLink = "mailto:test@example.com?subject=Test&body=This is a test";
    int width = 300;
    int height = 300;

    WritableImage qrImage = BaseController.generateQRCodeImage(mailtoLink, width, height);

    boolean hasNonWhitePixel = false;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        assert qrImage != null;
        if (qrImage.getPixelReader().getColor(x, y).getOpacity() > 0) {
          hasNonWhitePixel = true;
          break;
        }
      }
      if (hasNonWhitePixel) {
        break;
      }
    }

    assertTrue(hasNonWhitePixel, "QR code image should have non-white pixels");
  }

  @Test
  void testGenerateQRCodeImageWithInvalidLink() {
    String mailtoLink = "mailto:test@example.com?subject=Test";
    int width = 300;
    int height = 300;

    WritableImage qrImage = BaseController.generateQRCodeImage(mailtoLink, width, height);

    assertNotNull(qrImage, "QR code image should not be null for invalid link format");
    assertEquals(width, (int) qrImage.getWidth(), "QR code image width should match");
    assertEquals(height, (int) qrImage.getHeight(), "QR code image height should match");
  }

  @Test
  void testQRCodeImageSize() {
    String mailtoLink = "mailto:test@example.com?subject=Test&body=This is a test";
    int width = 300;
    int height = 300;

    WritableImage qrImage = BaseController.generateQRCodeImage(mailtoLink, width, height);

    assert qrImage != null;
    assertTrue(qrImage.getWidth() > 0, "QR code image width should be greater than 0");
    assertTrue(qrImage.getHeight() > 0, "QR code image height should be greater than 0");
  }

  @Test
  void testGenerateQRCodeImageWithLargeData() {
    String mailtoLink =
        "mailto:test@example.com?subject=Test&body=" + "a".repeat(1000);
    int width = 300;
    int height = 300;

    WritableImage qrImage = BaseController.generateQRCodeImage(mailtoLink, width, height);

    assertNotNull(qrImage, "QR code image should not be null for large input data");
    assertEquals(width, (int) qrImage.getWidth(), "QR code image width should match");
    assertEquals(height, (int) qrImage.getHeight(), "QR code image height should match");
  }
}
