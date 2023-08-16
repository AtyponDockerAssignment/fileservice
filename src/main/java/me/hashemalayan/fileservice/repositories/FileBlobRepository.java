package me.hashemalayan.fileservice.repositories;

import me.hashemalayan.fileservice.domain.FileBlob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileBlobRepository extends JpaRepository<FileBlob, Long> {
}
