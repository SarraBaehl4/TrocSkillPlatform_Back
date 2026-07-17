package RNCP.TrocSkillHub.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AvatarConstants {

    private AvatarConstants() {
    }

    public static final int AVATAR_COUNT = 50;

    public static final List<String> AVAILABLE_AVATAR_IDS = IntStream.rangeClosed(1, AVATAR_COUNT)
            .mapToObj(i -> "avatar" + i)
            .collect(Collectors.toList());

    public static boolean isValidAvatarId(String avatarId) {
        return AVAILABLE_AVATAR_IDS.contains(avatarId);
    }
}