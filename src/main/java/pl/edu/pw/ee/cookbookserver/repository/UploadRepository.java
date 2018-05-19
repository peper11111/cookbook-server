package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Upload;

@Repository
public interface UploadRepository extends CrudRepository<Upload, Long> {
}
