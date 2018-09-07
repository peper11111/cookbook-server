package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.CommentHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.service.CommentService;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentHelper commentHelper;
    private CommentRepository commentRepository;
    private UserHelper userHelper;

    @Autowired
    public CommentServiceImpl(CommentHelper commentHelper, CommentRepository commentRepository, UserHelper userHelper) {
        this.commentHelper = commentHelper;
        this.commentRepository = commentRepository;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity delete(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Comment comment = commentHelper.getComment(id);

        if (!currentUser.getId().equals(comment.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
