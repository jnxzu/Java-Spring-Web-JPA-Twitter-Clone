package twitter.clone.chirper.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentUser {
    private ChirperUser current;
}