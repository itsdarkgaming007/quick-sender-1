package com.quickreach.sender;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

public class WhatsAppAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        CharSequence pkgChar = event.getPackageName();
        if (pkgChar == null) return;
        String pkg = pkgChar.toString();

        if (pkg.equals("com.whatsapp") || pkg.equals("com.whatsapp.w4b")) {
            dismissInvalidDialog(rootNode);
            triggerSendButton(rootNode);
        }
    }

    private void dismissInvalidDialog(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> okButtons = rootNode.findAccessibilityNodeInfosByText("OK");
        if (okButtons != null) {
            for (AccessibilityNodeInfo btn : okButtons) {
                if (btn.isClickable()) {
                    btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                }
            }
        }
    }

    private void triggerSendButton(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> sendById = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if (sendById != null && !sendById.isEmpty() && sendById.get(0).isEnabled()) {
            sendById.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return;
        }

        List<AccessibilityNodeInfo> sendByText = rootNode.findAccessibilityNodeInfosByText("Send");
        if (sendByText != null) {
            for (AccessibilityNodeInfo node : sendByText) {
                if (node.isClickable()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {}
}