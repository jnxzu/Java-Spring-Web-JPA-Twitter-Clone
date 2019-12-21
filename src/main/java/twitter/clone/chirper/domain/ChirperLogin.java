package twitter.clone.chirper.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChirperLogin {
    @NotNull
    @Size(min = 6, max = 24)
    private String nickname;
    @NotNull
    @Size(min = 8, max = 24)
    private String password;
}