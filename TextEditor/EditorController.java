import java.io.File;

public class EditorController {
	EditorModel model;
	
	public EditorController(EditorModel model) {
		this.model = model;
	}
	
	public void setContent(String content) {
		model.setContent(content);
	}
}
