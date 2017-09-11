package enums;

import com.jh.Interceptor.Add;
import com.jh.Interceptor.Delete;
import com.jh.Interceptor.Find;
import com.jh.Interceptor.SaveOrUpdate;

public enum Annotation {
	ADD(Add.class), FIND(Find.class), DELETE(Delete.class), UPDATE(SaveOrUpdate.class);
	private Annotation(Object obj) {
	}
}
