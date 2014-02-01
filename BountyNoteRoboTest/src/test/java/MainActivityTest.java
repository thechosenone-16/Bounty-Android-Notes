package test.java;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.harsh.bountynotes.NotesHomeActivity;
import com.harsh.bountynotes.R;

import static org.fest.assertions.api.ANDROID.assertThat;
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	private NotesHomeActivity activity;
	 EditText quicknoteedittext;
	 Button button;
	 
	@Before
	public void setup() {
		activity = Robolectric.buildActivity(NotesHomeActivity.class).create().get();
		quicknoteedittext = (EditText) activity.findViewById(R.id.quicknoteedittext);
		button = (Button) activity.findViewById(R.id.quicknotedone);
	
	}
	@Test
	public void shouldNotBeNull() {
	  assertThat(activity).isNotNull();
	  
	 
	  assertThat(quicknoteedittext).isNotNull();

	 
	  assertThat(button).isNotNull();
	}
	
	@Test
	public void shouldProduceNoteOnButtonClick(){
		quicknoteedittext.setText("Hello");
		button.performClick();
		
	}
  @Test
  public void shouldFail() {
      assertTrue(true);
  }
}