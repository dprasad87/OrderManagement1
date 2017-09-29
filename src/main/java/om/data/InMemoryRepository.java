package om.data;

import om.domain.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public abstract class InMemoryRepository<T extends Identifiable> {

    @Autowired
    private IdGenerator idGenerator;

    private List<T> elements = Collections.synchronizedList(new ArrayList<T>());

    //Create
    public T create(T element) {
        elements.add(element);
        element.setId(idGenerator.getNextLong());
        return element;
    }

    //Delete
    public boolean delete(Long id) {

        return elements.removeIf(element -> element.getId().equals(id));
    }

    //Retrieve
    public List<T> findAll() {
        return elements;
    }

    public Optional<T> findById(Long id) {
        return elements.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public int getCount() {
        return elements.size();
    }

    public void clear() {
        elements.clear();
    }

    public boolean update(Long id, T updated) {
        if( updated == null ) {
            return false;
        }
        else {
            Optional<T> elemtnt = findById(id);
            elemtnt.ifPresent(original -> updateIfExists(original , updated));
            return elemtnt.isPresent();
        }
    }

    protected abstract void updateIfExists(T original, T desired);

}
