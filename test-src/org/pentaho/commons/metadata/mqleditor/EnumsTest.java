package org.pentaho.commons.metadata.mqleditor;

import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Basic;
import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Complex;
import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Subclassing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Tests enums over RPC.
 */
public class EnumsTest extends GWTTestCase {
  private static final int TEST_DELAY = 5000;

  private static EnumsTestServiceAsync getService() {
    EnumsTestServiceAsync service = GWT.create(EnumsTestService.class);
    ServiceDefTarget target = (ServiceDefTarget) service;
    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "enums");
    return service;
  }

  private static void rethrowException(Throwable caught) {
    if (caught instanceof RuntimeException) {
      throw (RuntimeException) caught;
    } else {
      throw new RuntimeException(caught);
    }
  }

  @Override
  public String getModuleName() {
    return "com.google.gwt.user.RPCSuite";
  }

  /**
   * Test that basic enums can be used over RPC.
   */
  public void testBasicEnums() {
    delayTestFinish(TEST_DELAY);
    getService().echo(Basic.A, new AsyncCallback<Basic>() {
      public void onFailure(Throwable caught) {
        rethrowException(caught);
      }

      public void onSuccess(Basic result) {
        assertEquals(Basic.A, result);
        finishTest();
      }
    });
  }

  /**
   * Test that complex enums with state and non-default constructors can be used
   * over RPC and that the client state does not change.
   */
  public void testComplexEnums() {
    delayTestFinish(TEST_DELAY);

    Complex a = Complex.A;
    a.value = "client";

    getService().echo(Complex.A, new AsyncCallback<Complex>() {
      public void onFailure(Throwable caught) {
        rethrowException(caught);
      }

      public void onSuccess(Complex result) {
        assertEquals(Complex.A, result);

        // Ensure that the server's changes did not impact us.
        assertEquals("client", result.value);

        finishTest();
      }
    });
  }

  /**
   * Test that null can be used as an enumeration.
   */
  public void testNull() {
    delayTestFinish(TEST_DELAY);

    getService().echo((Basic) null, new AsyncCallback<Basic>() {
      public void onFailure(Throwable caught) {
        rethrowException(caught);
      }

      public void onSuccess(Basic result) {
        assertNull(result);
        finishTest();
      }
    });
  }

  /**
  * Test that enums with subclasses can be passed over RPC.
   */
  public void testSubclassingEnums() {
    delayTestFinish(TEST_DELAY);

    getService().echo(Subclassing.A, new AsyncCallback<Subclassing>() {
      public void onFailure(Throwable caught) {
        rethrowException(caught);
      }

      public void onSuccess(Subclassing result) {
        assertEquals(Subclassing.A, result);
        finishTest();
      }
    });
  }
}