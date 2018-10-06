package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Upload;

@Repository
public interface UploadRepository extends CrudRepository<Upload, Long> {

    @Query(value = "SELECT * FROM cb_upload WHERE owner_id = :ownerId AND id NOT IN (SELECT avatar_id FROM cb_user WHERE avatar_id IS NOT NULL) AND id NOT IN (SELECT banner_id FROM cb_user WHERE banner_id IS NOT NULL) AND id NOT IN (SELECT banner_id FROM cb_recipe WHERE banner_id IS NOT NULL)", nativeQuery = true)
    Iterable<Upload> findUnusedUploads(@Param("ownerId") Long ownerId);
}
