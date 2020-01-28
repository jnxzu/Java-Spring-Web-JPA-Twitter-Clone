package twitter.clone.chirper.service;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitter.clone.chirper.domain.ChirperUser;;

public class ReactiveUserDao {
    private ConnectionFactory cf;

    public ReactiveUserDao() {
        this.cf = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder().host("localhost")
                .username("postgres").password("admin").database("postgres").build());
    }

    public Mono<ChirperUser> findOneById(int id) {
        return Mono.from(cf.create())
                .flatMap(c -> Mono.from(
                        c.createStatement("select * from chirper_user where user_id = $1").bind("$1", id).execute())
                        .doFinally((st) -> close(c)))
                .map(result -> result.map((row, meta) -> new ChirperUser(row.get("user_id", Integer.class),
                        row.get("first_name", String.class), row.get("last_name", String.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(p -> Mono.from(p));
    }

    public Flux<ChirperUser> findAll() {
        return Mono.from(cf.create()).flatMap(
                (c) -> Mono.from(c.createStatement("select * from chirper_user").execute()).doFinally((st) -> close(c)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    ChirperUser usr = new ChirperUser(row.get("user_id", Integer.class),
                            row.get("first_name", String.class), row.get("last_name", String.class),
                            row.get("nick", String.class), row.get("password", String.class),
                            row.get("email", String.class));
                    return usr;
                })));
    }

    public Mono<ChirperUser> addUser(ChirperUser cu) {
        return Mono.from(cf.create())
                .flatMap(c -> Mono.from(c.beginTransaction()).then(Mono.from(c.createStatement(
                        "insert into chirper_user(email,first_name,last_name,nick,password) values($1,$2,$3,$4,$5)")
                        .bind("$1", cu.getEmail()).bind("$2", cu.getFirstName()).bind("$3", cu.getLastName())
                        .bind("$4", cu.getNick()).bind("$5", cu.getPassword()).returnGeneratedValues("user_id")
                        .execute()))
                        .map(result -> result.map((row, meta) -> new ChirperUser(row.get("user_id", Integer.class),
                                cu.getFirstName(), cu.getLastName(), cu.getNick(), cu.getPassword(), cu.getEmail())))
                        .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction())
                        .doFinally((st) -> c.close()));
    }

    public Mono<ChirperUser> deleteUser(int id) {
        return Mono.from(cf.create()).flatMap(c -> Mono.from(c.beginTransaction()).then(Mono.from(c.createStatement(
                "delete from message where msg_id in (select messages_msg_id from message_authors where authors_user_id = $1)")
                .bind("$1", id).execute()))
                .then(Mono.from(c.createStatement("delete from chirper_user where user_id = $1 returning *")
                        .bind("$1", id).execute()))
                .map(result -> result.map((row, meta) -> new ChirperUser(row.get("user_id", Integer.class),
                        row.get("first_name", String.class), row.get("last_name", String.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    public Mono<ChirperUser> changeNickname(int id, String nickname) {
        return Mono.from(cf.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("update chirper_user set nick = $2 where user_id = $1 returning *")
                        .bind("$1", id).bind("$2", nickname).execute()))
                .map(result -> result.map((row, meta) -> new ChirperUser(row.get("user_id", Integer.class),
                        row.get("first_name", String.class), row.get("last_name", String.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close()).then(Mono.empty());
    }
}
