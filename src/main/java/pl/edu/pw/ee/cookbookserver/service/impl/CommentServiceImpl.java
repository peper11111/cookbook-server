package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.CommentHelper;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.service.CommentService;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {

    private CommentHelper commentHelper;
    private CommentRepository commentRepository;
    private PayloadHelper payloadHelper;
    private UserHelper userHelper;

    @Autowired
    public CommentServiceImpl(CommentHelper commentHelper, CommentRepository commentRepository,
                              PayloadHelper payloadHelper, UserHelper userHelper) {
        this.commentHelper = commentHelper;
        this.commentRepository = commentRepository;
        this.payloadHelper = payloadHelper;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity create(JSONObject payload) throws Exception {
        User currentUser = userHelper.getCurrentUser();

        Comment comment = new Comment();
        comment.setAuthor(currentUser);
        comment.setRecipe(payloadHelper.getValidRecipe(payload));
        comment.setContent(payloadHelper.getValidContent(payload));
        if (payload.has(PayloadKey.PARENT_ID.value())) {
            comment.setParent(payloadHelper.getValidParent(payload));
        }
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Comment comment = commentHelper.getComment(id);
        CommentDto commentDto = commentHelper.mapCommentToCommentDto(comment);
        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
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
