package twitter.clone.chirper.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitter.clone.chirper.domain.Message;

public class ReactiveMessageDao {
    private ConnectionFactory cf;

    public ReactiveMessageDao() {
        this.cf = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder().host("localhost")
                .username("postgres").password("admin").database("postgres").build());
    }

    public Mono<Message> findOneById(int id) {
        return Mono.from(cf.create())
                .flatMap(c -> Mono
                        .from(c.createStatement("select * from message where msg_id = $1").bind("$1", id).execute())
                        .doFinally((st) -> close(c)))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("has_image", Boolean.class))))
                .flatMap(p -> Mono.from(p));
    }

    public Flux<Message> findAll() {
        return Mono.from(cf.create()).flatMap(
                (c) -> Mono.from(c.createStatement("select * from message").execute()).doFinally((st) -> close(c)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    Message msg = new Message(row.get("msg_id", Integer.class), row.get("content", String.class),
                            Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                            row.get("has_image", Boolean.class));
                    return msg;
                })));
    }

    public Mono<Message> deleteMessage(int id) {
        return Mono.from(cf.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("delete from message where msg_id = $1 returning *").bind("$1", id)
                        .execute()))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("has_image", Boolean.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    public Mono<Message> editMessage(int id, String content) {
        return Mono.from(cf.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("update message set content = $2 where msg_id = $1 returning *")
                        .bind("$1", id).bind("$2", content).execute()))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("has_image", Boolean.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close()).then(Mono.empty());
    }
}