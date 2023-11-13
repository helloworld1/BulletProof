package org.liberty.multi.bulletproof.android;

import android.content.Context;
import android.content.Intent;

import org.liberty.multi.bulletproof.R;
import org.liberty.multi.bulletproof.resolver.ShareResolver;

public class AndroidShareResolver implements ShareResolver {
    private Context context;
    public AndroidShareResolver(Context context) {
        this.context = context;
    }

    @Override
    public void share(String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.share_title));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);    

        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_text)));
    }

}
