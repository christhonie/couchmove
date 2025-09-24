package com.github.couchmove.pojo;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.java.Bucket;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.vdurmont.semver4j.Semver.SemverType.LOOSE;
import static lombok.AccessLevel.PRIVATE;

/**
 * a {@link CouchbaseEntity} representing a change in Couchbase {@link Bucket}
 *
 * @author ctayeb
 * Created on 27/05/2017
 */
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@Data
public class ChangeLog extends CouchbaseEntity implements Comparable<ChangeLog> {

    /**
     * The version of the change
     */
    private String version;

    /**
     * The execution order of the change
     */
    private Integer order;

    /**
     * The description of the change
     */
    private String description;

    /**
     * The {@link Type} of the change
     */
    private Type type;

    /**
     * The script file or folder that was executed in the change
     */
    private String script;

    /**
     * A unique identifier of the file or folder of the change.
     */
    private String checksum;

    /**
     * Legacy identifier of the file or folder of the change.
     * {@link #checksum} now represents a checksum based on the normalized content of the file or folder.
     * Used for backward compatibility when migrating from an older version.
     */
    @JsonIgnore
    private String checksumLegacy;

    /**
     * The OS username of the process that executed the change
     */
    private String runner;

    /**
     * Date of execution of the change
     */
    private Date timestamp;

    /**
     * The duration of the execution of the change in milliseconds
     */
    private Long duration;

    /**
     * The {@link Status} of the change
     */
    private Status status;

    @Override
    public int compareTo(@NotNull ChangeLog o) {
        if (version == null && o.version == null) {
            return 0;
        }
        if (version == null) {
            return -1;
        }
        if (o.version == null) {
            return 1;
        }
        try {
            return new Semver(version, LOOSE).compareTo(new Semver(o.version, LOOSE));
        } catch (SemverException e) {
            return version.compareTo(o.version);
        }
    }
}
