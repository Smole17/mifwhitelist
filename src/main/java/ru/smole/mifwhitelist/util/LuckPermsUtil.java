package ru.smole.mifwhitelist.util;

import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class LuckPermsUtil {

    public void getUser(UUID uuid, Consumer<User> consumer) {
        LuckPermsProvider.get()
                .getUserManager()
                .loadUser(uuid)
                .thenAccept(consumer);
    }
}
