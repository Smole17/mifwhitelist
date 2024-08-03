package ru.smole.mifwhitelist.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class LuckPermsUtil {

    @SneakyThrows
    public User getUser(UUID uuid) {
        return LuckPermsProvider.get()
                .getUserManager()
                .loadUser(uuid)
                .get();
    }
}
