package com.lutshe.doiter.data.provider.stub;

import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.MessagesProvider;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class MessagesProviderStub implements MessagesProvider {

    private Message [] messages = {
            new Message(0L, "Life is not a bed of roses.", 0L, 0L),
            new Message(1L, "Life is not all cakes and ale (beer and skittles).", 0L, 1L),
            new Message(2L, "Like a cat on hot bricks.", 1L, 0L),
            new Message(3L, "Like a needle in a haystack.", 1L, 1L),
            new Message(4L, "Like begets like.", 2L, 0L),
            new Message(5L, "Like cures like.", 2L, 1L),
            new Message(6L, "Like father, like son.", 3L, 0L),
            new Message(7L, "Little things amuse little minds.", 3L, 1L),
            new Message(8L, "Little thieves are hanged, but great ones escape.", 4L, 0L),
            new Message(9L, "A new broom sweeps clean.", 4L, 1L),
            new Message(10L, "A quiet conscience sleeps in thunder.", 5L, 0L),
            new Message(11L, "A silent fool is counted wise.", 5L, 1L),
            new Message(12L, "A stitch in time saves nine.", 6L, 0L),
            new Message(13L, "A storm in a teacup.", 6L, 1L),
            new Message(14L, "A threatened blow is seldom given.", 7L, 0L),
            new Message(15L, "As drunk as a lord.", 7L, 1L),
            new Message(16L, "As like as two peas.", 8L, 0L),
            new Message(17L, "Keep your mouth shut and your eyes open.", 8L, 0L),
            new Message(18L, "Lies have short legs.", 9L, 0L),
            new Message(19L, "Marriages are made in heaven.", 9L, 1L),
            new Message(20L, "Marriages are made in heaven.", 0L, 2L),
            new Message(21L, "Misfortunes never come alone (singly).", 0L, 3L),
            new Message(22L, "Life is not a bed of roses.", 0L, 4L)

    };

    @Override
    public Message[] getAllMessages() {
        return messages;
    }
}
