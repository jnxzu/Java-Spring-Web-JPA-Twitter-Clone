package twitter.clone.chirper.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserState {
    private boolean admin;
    private boolean logged;
}