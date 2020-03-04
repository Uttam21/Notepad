package com.example.notepad.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity(primaryKeys = {"id","version"})
public class Note implements Parcelable {


    private int id=0;
    private String title;
    private String  noteText;
    private long createdDateTime;
    private long modifiedDateTime;
    @ColumnInfo(name = "version")
    private int version;
    private ArrayList<String> imageListParsedPath=null;





    public Note(int id, String title, String noteText, long createdDateTime,
                long modifiedDateTime, int version, ArrayList<String> imageListParsedPath) {
        this.id = id;
        this.title = title;
        this.noteText = noteText;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
        this.version = version;
        this.imageListParsedPath = imageListParsedPath;
    }
    @Ignore
    public Note(String title, String noteText, long createdDateTime, long modifiedDateTime) {
        this.title = title;
        this.noteText = noteText;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        noteText = in.readString();
        createdDateTime = in.readLong();
        modifiedDateTime = in.readLong();
        version = in.readInt();
        imageListParsedPath = in.createStringArrayList();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(noteText);
        dest.writeLong(createdDateTime);
        dest.writeLong(modifiedDateTime);
        dest.writeInt(version);
        dest.writeList(imageListParsedPath);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public long getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(long modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<String> getImageListParsedPath() {
        return imageListParsedPath;
    }

    public void setImageListParsedPath(ArrayList<String> imageListParsedPath) {
        this.imageListParsedPath = imageListParsedPath;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", modifiedDateTime=" + modifiedDateTime +
                ", version=" + version +
                ", imageListParsedPath='" + imageListParsedPath + '\'' +
                '}';
    }






}
