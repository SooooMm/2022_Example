package app;

public class SignUpForm extends BaseFrame{
	public SignUpForm() {
		super("회원가입", 500, 400);
		setBorderLayout();
		
	}
	public static void main(String[] args) {
		new SignUpForm().setVisible(true);
	}

}
