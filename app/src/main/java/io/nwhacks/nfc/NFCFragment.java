package io.nwhacks.nfc;

import android.content.Intent;
import android.nfc.NfcManager;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by rice on 12/28/17.
 */

public abstract class NFCFragment extends Fragment {
    abstract public void tagDiscovered(NFCManager mgr, Intent intent);
    abstract public void loggedIn(FirebaseUser user);
}
