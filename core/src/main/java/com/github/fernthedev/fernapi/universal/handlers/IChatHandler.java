package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;

public interface IChatHandler<NewMessage> {

    NewMessage parseComponent(BaseMessage baseMessage);

}
