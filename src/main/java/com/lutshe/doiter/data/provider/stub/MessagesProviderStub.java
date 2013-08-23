package com.lutshe.doiter.data.provider.stub;

import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.MessagesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class MessagesProviderStub implements MessagesProvider {

    private Random random = new Random();

    private String [] strings = {
            "Life is not a bed of roses.",
            "Life is not all cakes and ale (beer and skittles).",
            "Like a cat on hot bricks.",
            "Like a needle in a haystack.",
            "Like begets like.",
            "Like cures like.",
            "Like father, like son.",
            "Little things amuse little minds.",
            "Little thieves are hanged, but great ones escape.",
            "A new broom sweeps clean.",
            "A quiet conscience sleeps in thunder.",
            "A silent fool is counted wise.",
            "A stitch in time saves nine.",
            "A storm in a teacup.",
            "A threatened blow is seldom given.",
            "As drunk as a lord.",
            "As like as two peas.",
            "Keep your mouth shut and your eyes open.",
            "Lies have short legs.",
            "Marriages are made in heaven.",
            "Misfortunes never come alone (singly)."
    };

    private List<Message> getRandomMessagesList(Long goalId){
        List<Message> result = new ArrayList<Message>();
        int count = random.nextInt(10)+2;
        for (int i = 0; i < count; i++){
            long index = getRandomIndex();
            result.add(new Message(index, strings[(int)index], goalId));
        }
        return result;
    }

    private int getRandomIndex(){
        return random.nextInt(21);
    }

    @Override
    public List<Message> getMessages(Long goalId) {
        return getRandomMessagesList(goalId);
    }

    @Override
    public List<Message> getMessages(Long goalId, Long lastMessageId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Message getRandomMessage(Long goalId) {
        long index = getRandomIndex();
        return new Message(goalId*100 + index, strings[(int)index], goalId);
    }
}
